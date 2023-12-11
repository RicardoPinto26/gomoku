## Introduction

Single Page frontend application built using React and Material-UI.
This application is a client of the Gomoku Royale API, [documented here](Gomoku-BackendDoc.md)


## Application Architecture ##

The frontend is organized as follows:
* [`/public`](../code/js/gomoku-app/public): Contains the index.html file, the favicon.ico file and the manifest.json file;
* [`/src`](../code/js/gomoku-app/src)
  * [`/components`](../code/js/gomoku-app/src/components): Contains the React components and pages used in the application;
  * [`/domain`](../code/js/gomoku-app/src/domain): Contains the domain classes used in the application;
  * [`/http`](../code/js/gomoku-app/src/http): Contains the Siren Entity classes received in HTTP responses;
  * [`/resources`](../code/js/gomoku-app/src/resources): Contains the images and other assets used in the application;
  * [`/services`](../code/js/gomoku-app/src/services): Contains the services used in the application; this layer is responsible for the communication with the API;
  * [`/utils`](../code/js/gomoku-app/src/utils): Contains the utility classes used in the application;
  * [`App.tsx`](../code/js/gomoku-app/src/App.tsx): The entry point for the gomoku application;
  * [`index.tsx`](../code/js/gomoku-app/src/index.tsx): The entry point of the application;

## Authentication ##

The user authentication is done via the Login or Register pages.

The UserManager type was implemented to store the user information in the application. 
This user information is managed by the LoggedInContext.

The AuthnContainer component provides the LoggedInContext to the children components, so they can access the user information
using the useUserManager hook.

The user session is stored in the browser's local storage.
