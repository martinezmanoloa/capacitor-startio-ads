import { registerPlugin } from '@capacitor/core';

import type { StartioAdsPlugin } from './definitions';

const StartioAds = registerPlugin<StartioAdsPlugin>('StartioAds', {
  web: () => import('./web').then((m) => new m.StartioAdsWeb()),
});

export * from './definitions';
export { StartioAds };
