import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonnel } from '../personnel.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-personnel-detail',
  templateUrl: './personnel-detail.component.html',
})
export class PersonnelDetailComponent implements OnInit {
  personnel: IPersonnel | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personnel }) => {
      this.personnel = personnel;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
