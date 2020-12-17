import {Component, Input} from '@angular/core'

@Component({
  selector: 'app-square',
  template: `
    <div class="alert alert-info m-0 align-text-bottom text-center square-box align-middle" role="alert">
      <h1 class="display-3">{{ value }}</h1>
    </div>
  `,
  styles: ['.square-box {min-height: 125px; min-width: 125px; border-radius: 0 !important}']
})
export class SquareComponent {

  @Input() value: 'X' | 'O' | '' = ''

  isEmpty() {
    return this.value === '';
  }
}
