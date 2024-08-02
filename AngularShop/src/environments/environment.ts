// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

//export const BASE_URL = 'http://192.168.1.129';
export const BASE_URL = 'https://achau-server.com';

export const environment = {
  production: false,
  backendAPI: `${BASE_URL}/api`,
  fileServerAPI: `${BASE_URL}/files`,
  keycloakAPI: `${BASE_URL}/auth`,
  keycloakRealm : "E-Commerce",
  keycloakClient : "E-Commerce-Client"
};

/*
backendAPI: "https://achau-server.com/api",
fileServerAPI:"https://achau-server.com/files",
keycloakAPI:"https://achau-server.com/auth",
*/

/*
  backendAPI: "http://10.2.109.121/api",
  fileServerAPI:"http://10.2.109.121/files",
  keycloakAPI:"http://10.2.109.121/auth",
*/

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
