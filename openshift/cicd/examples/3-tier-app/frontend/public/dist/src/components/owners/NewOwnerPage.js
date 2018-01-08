"use strict";
const React = require('react');
const OwnerEditor_1 = require('./OwnerEditor');
const newOwner = () => ({
    id: null,
    isNew: true,
    firstName: '',
    lastName: '',
    address: '',
    city: '',
    telephone: '',
    pets: []
});
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = () => React.createElement(OwnerEditor_1.default, {initialOwner: newOwner()});
//# sourceMappingURL=NewOwnerPage.js.map