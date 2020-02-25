import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IUsuario, Usuario } from 'app/shared/model/usuario.model';
import { UsuarioService } from './usuario.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IElementoLista } from 'app/shared/model/elemento-lista.model';
import { ElementoListaService } from 'app/entities/elemento-lista/elemento-lista.service';

type SelectableEntity = IUser | IElementoLista;

@Component({
  selector: 'jhi-usuario-update',
  templateUrl: './usuario-update.component.html'
})
export class UsuarioUpdateComponent implements OnInit {
  isSaving = false;

  users: IUser[] = [];

  elementolistas: IElementoLista[] = [];

  editForm = this.fb.group({
    id: [],
    segundoNombre: [],
    segundoApellido: [],
    numDocumento: [null, [Validators.required, Validators.maxLength(15)]],
    celular: [null, [Validators.required]],
    direccion: [],
    genero: [],
    userId: [],
    cuidadId: [null, Validators.required]
  });

  constructor(
    protected usuarioService: UsuarioService,
    protected userService: UserService,
    protected elementoListaService: ElementoListaService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuario }) => {
      this.updateForm(usuario);

      this.userService
        .query()
        .pipe(
          map((res: HttpResponse<IUser[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IUser[]) => (this.users = resBody));

      this.elementoListaService
        .query()
        .pipe(
          map((res: HttpResponse<IElementoLista[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IElementoLista[]) => (this.elementolistas = resBody));
    });
  }

  updateForm(usuario: IUsuario): void {
    this.editForm.patchValue({
      id: usuario.id,
      segundoNombre: usuario.segundoNombre,
      segundoApellido: usuario.segundoApellido,
      numDocumento: usuario.numDocumento,
      celular: usuario.celular,
      direccion: usuario.direccion,
      genero: usuario.genero,
      userId: usuario.userId,
      cuidadId: usuario.cuidadId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const usuario = this.createFromForm();
    if (usuario.id !== undefined) {
      this.subscribeToSaveResponse(this.usuarioService.update(usuario));
    } else {
      this.subscribeToSaveResponse(this.usuarioService.create(usuario));
    }
  }

  private createFromForm(): IUsuario {
    return {
      ...new Usuario(),
      id: this.editForm.get(['id'])!.value,
      segundoNombre: this.editForm.get(['segundoNombre'])!.value,
      segundoApellido: this.editForm.get(['segundoApellido'])!.value,
      numDocumento: this.editForm.get(['numDocumento'])!.value,
      celular: this.editForm.get(['celular'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      genero: this.editForm.get(['genero'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      cuidadId: this.editForm.get(['cuidadId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsuario>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
