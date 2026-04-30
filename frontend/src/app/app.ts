import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatToolbarModule,
    RouterLink,
    RouterLinkActive,
    RouterOutlet,
  ],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly navigationItems = [
    {
      label: 'Clients',
      route: '/clients',
      icon: 'groups',
    },
    {
      label: 'Rendez-vous',
      route: '/rendez-vous',
      icon: 'event',
    },
    {
      label: 'Portail client',
      route: '/portail-client',
      icon: 'loyalty',
    },
  ];

  protected readonly overviewItems = [
    {
      kicker: 'Base clients',
      title: 'Fiches centralisées',
      description: 'Création, mise à jour et suivi des coordonnées de contact.',
      icon: 'badge',
    },
    {
      kicker: 'Planning',
      title: 'Rendez-vous pilotés',
      description: 'Organisation du jour, filtres rapides et statuts visibles.',
      icon: 'schedule',
    },
    {
      kicker: 'Fidélisation',
      title: 'Points attribués automatiquement',
      description: '100 points crédités lorsqu’un rendez-vous est honoré.',
      icon: 'stars',
    },
  ];
}
