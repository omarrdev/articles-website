import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { AdminService } from '../../../services/admin.service';
import { ConfirmDialogComponent } from '../../../shared/confirm-dialog/confirm-dialog.component';
import { User } from '../../../models';

@Component({
  selector: 'app-admin-users',
  standalone: true,
  imports: [FormsModule, DatePipe, ConfirmDialogComponent],
  templateUrl: './admin-users.component.html'
})
export class AdminUsersComponent implements OnInit {
  private adminService = inject(AdminService);
  users = signal<User[]>([]);
  roleEdits: Record<number, string> = {};
  confirmVisible = false;
  pendingDeleteId: number | null = null;

  ngOnInit(): void {
    this.adminService.getUsers().subscribe(u => {
      this.users.set(u);
      u.forEach(user => this.roleEdits[user.id] = user.role);
    });
  }

  saveRole(userId: number): void {
    this.adminService.updateUserRole(userId, this.roleEdits[userId]).subscribe(u =>
      this.users.update(list => list.map(x => x.id === userId ? u : x))
    );
  }

  confirmDelete(id: number): void { this.pendingDeleteId = id; this.confirmVisible = true; }

  onConfirmed(yes: boolean): void {
    this.confirmVisible = false;
    if (yes && this.pendingDeleteId) {
      this.adminService.deleteUser(this.pendingDeleteId).subscribe(() =>
        this.users.update(list => list.filter(u => u.id !== this.pendingDeleteId))
      );
    }
    this.pendingDeleteId = null;
  }
}
