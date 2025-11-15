export interface StartioAdsPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
