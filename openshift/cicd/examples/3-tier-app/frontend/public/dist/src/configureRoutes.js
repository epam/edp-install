"use strict";
const React = require('react');
const react_router_1 = require('react-router');
const App_1 = require('./components/App');
const WelcomePage_1 = require('./components/WelcomePage');
const FindOwnersPage_1 = require('./components/owners/FindOwnersPage');
const OwnersPage_1 = require('./components/owners/OwnersPage');
const NewOwnerPage_1 = require('./components/owners/NewOwnerPage');
const EditOwnerPage_1 = require('./components/owners/EditOwnerPage');
const NewPetPage_1 = require('./components/pets/NewPetPage');
const EditPetPage_1 = require('./components/pets/EditPetPage');
const VisitsPage_1 = require('./components/visits/VisitsPage');
const VetsPage_1 = require('./components/vets/VetsPage');
const ErrorPage_1 = require('./components/ErrorPage');
const NotFoundPage_1 = require('./components/NotFoundPage');
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = () => (React.createElement(react_router_1.Route, {component: App_1.default}, 
    React.createElement(react_router_1.Route, {path: '/', component: WelcomePage_1.default}), 
    React.createElement(react_router_1.Route, {path: '/owners/list', component: FindOwnersPage_1.default}), 
    React.createElement(react_router_1.Route, {path: '/owners/new', component: NewOwnerPage_1.default}), 
    React.createElement(react_router_1.Route, {path: '/owners/:ownerId/edit', component: EditOwnerPage_1.default}), 
    React.createElement(react_router_1.Route, {path: '/owners/:ownerId/pets/:petId/edit', component: EditPetPage_1.default}), 
    React.createElement(react_router_1.Route, {path: '/owners/:ownerId/pets/new', component: NewPetPage_1.default}), 
    React.createElement(react_router_1.Route, {path: '/owners/:ownerId/pets/:petId/visits/new', component: VisitsPage_1.default}), 
    React.createElement(react_router_1.Route, {path: '/owners/:ownerId', component: OwnersPage_1.default}), 
    React.createElement(react_router_1.Route, {path: '/vets', component: VetsPage_1.default}), 
    React.createElement(react_router_1.Route, {path: '/error', component: ErrorPage_1.default}), 
    React.createElement(react_router_1.Route, {path: '*', component: NotFoundPage_1.default})));
//# sourceMappingURL=configureRoutes.js.map