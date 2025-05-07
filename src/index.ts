// Reexport the native module. On web, it will be resolved to ExpoSmsAutofillModule.web.ts
// and on native platforms to ExpoSmsAutofillModule.ts
export { default } from './ExpoSmsAutofillModule';
export { default as ExpoSmsAutofillView } from './ExpoSmsAutofillView';
export * from  './ExpoSmsAutofill.types';
