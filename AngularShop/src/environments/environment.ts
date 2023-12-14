// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  backendAPI: "http://10.2.109.122/api",
  fileServerAPI:"http://10.2.109.122/files",
  keycloakAPI:"http://10.2.109.122/auth",
  keycloakRealm : "E-Commerce",
  keycloakClient : "E-Commerce-Client"
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
