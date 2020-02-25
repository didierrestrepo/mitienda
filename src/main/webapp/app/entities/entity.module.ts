import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'producto',
        loadChildren: () => import('./producto/producto.module').then(m => m.MitiendaProductoModule)
      },
      {
        path: 'stock',
        loadChildren: () => import('./stock/stock.module').then(m => m.MitiendaStockModule)
      },
      {
        path: 'producto-detalle',
        loadChildren: () => import('./producto-detalle/producto-detalle.module').then(m => m.MitiendaProductoDetalleModule)
      },
      {
        path: 'sucursal',
        loadChildren: () => import('./sucursal/sucursal.module').then(m => m.MitiendaSucursalModule)
      },
      {
        path: 'usuario',
        loadChildren: () => import('./usuario/usuario.module').then(m => m.MitiendaUsuarioModule)
      },
      {
        path: 'tipo-lista',
        loadChildren: () => import('./tipo-lista/tipo-lista.module').then(m => m.MitiendaTipoListaModule)
      },
      {
        path: 'elemento-lista',
        loadChildren: () => import('./elemento-lista/elemento-lista.module').then(m => m.MitiendaElementoListaModule)
      },
      {
        path: 'producto-categoria',
        loadChildren: () => import('./producto-categoria/producto-categoria.module').then(m => m.MitiendaProductoCategoriaModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class MitiendaEntityModule {}
