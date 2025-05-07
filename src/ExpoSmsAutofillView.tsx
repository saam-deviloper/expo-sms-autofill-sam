import { requireNativeView } from 'expo';
import * as React from 'react';

import { ExpoSmsAutofillViewProps } from './ExpoSmsAutofill.types';

const NativeView: React.ComponentType<ExpoSmsAutofillViewProps> =
  requireNativeView('ExpoSmsAutofill');

export default function ExpoSmsAutofillView(props: ExpoSmsAutofillViewProps) {
  return <NativeView {...props} />;
}
