import { registerWebModule, NativeModule } from 'expo';

import { ExpoSmsAutofillModuleEvents } from './ExpoSmsAutofill.types';

class ExpoSmsAutofillModule extends NativeModule<ExpoSmsAutofillModuleEvents> {
  PI = Math.PI;
  async setValueAsync(value: string): Promise<void> {
    this.emit('onChange', { value });
  }
  hello() {
    return 'Hello world! 👋';
  }
}

export default registerWebModule(ExpoSmsAutofillModule, 'ExpoSmsAutofillModule');
