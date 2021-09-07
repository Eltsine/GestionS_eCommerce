import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEntreprise } from '../entreprise.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-entreprise-detail',
  templateUrl: './entreprise-detail.component.html',
})
export class EntrepriseDetailComponent implements OnInit {
  entreprise: IEntreprise | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entreprise }) => {
      this.entreprise = entreprise;
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
