import * as React from 'react';

import { ExpoSmsAutofillViewProps } from './ExpoSmsAutofill.types';

export default function ExpoSmsAutofillView(props: ExpoSmsAutofillViewProps) {
  return (
    <div>
      <iframe
        style={{ flex: 1 }}
        src={props.url}
        onLoad={() => props.onLoad({ nativeEvent: { url: props.url } })}
      />
    </div>
  );
}
