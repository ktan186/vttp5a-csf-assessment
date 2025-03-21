import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-confirmation',
  standalone: false,
  templateUrl: './confirmation.component.html',
  styleUrl: './confirmation.component.css'
})
export class ConfirmationComponent implements OnInit {

  date: string = '';
  orderId: string = '';
  paymentId: string = '';
  total: string = '';

  private actRoute = inject(ActivatedRoute);

  // TODO: Task 5

  ngOnInit(): void {
    // confirm/:date/:orderId/:paymentId/:total
    const date = this.actRoute.snapshot.paramMap.get('date');
    if (date) {
      this.date = date;
    }
    const orderId = this.actRoute.snapshot.paramMap.get('orderId');
    if (orderId) {
      this.orderId = orderId;
    }
    const paymentId = this.actRoute.snapshot.paramMap.get('paymentId');
    if (paymentId) {
      this.paymentId = paymentId;
    }
    const total = this.actRoute.snapshot.paramMap.get('total');
    if (total) {
      this.total = total;
    }
  }

}
