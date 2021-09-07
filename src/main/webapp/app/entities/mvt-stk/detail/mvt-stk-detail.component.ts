import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMvtStk } from '../mvt-stk.model';

@Component({
  selector: 'jhi-mvt-stk-detail',
  templateUrl: './mvt-stk-detail.component.html',
})
export class MvtStkDetailComponent implements OnInit {
  mvtStk: IMvtStk | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mvtStk }) => {
      this.mvtStk = mvtStk;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
