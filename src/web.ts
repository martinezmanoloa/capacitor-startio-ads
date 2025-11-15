import { WebPlugin } from '@capacitor/core';

import type { StartioAdsPlugin } from './definitions';

export class StartioAdsWeb extends WebPlugin implements StartioAdsPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
