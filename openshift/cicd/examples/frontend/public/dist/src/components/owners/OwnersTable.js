"use strict";
const React = require('react');
const renderRow = (owner) => (React.createElement("tr", {key: owner.id}, 
    React.createElement("td", null, 
        React.createElement("a", {href: `/owners/${owner.id}`}, 
            owner.firstName, 
            " ", 
            owner.lastName)
    ), 
    React.createElement("td", {className: 'hidden-sm hidden-xs'}, owner.address), 
    React.createElement("td", null, owner.city), 
    React.createElement("td", null, owner.telephone), 
    React.createElement("td", {className: 'hidden-xs'}, owner.pets.map(pet => pet.name).join(', '))));
const renderOwners = (owners) => (React.createElement("section", null, 
    React.createElement("h2", null, 
        owners.length, 
        " Owners found"), 
    React.createElement("table", {className: 'table table-striped'}, 
        React.createElement("thead", null, 
            React.createElement("tr", null, 
                React.createElement("th", null, "Name"), 
                React.createElement("th", {className: 'hidden-sm hidden-xs'}, "Address"), 
                React.createElement("th", null, "City"), 
                React.createElement("th", null, "Telephone"), 
                React.createElement("th", {className: 'hidden-xs'}, "Pets"))
        ), 
        React.createElement("tbody", null, owners.map(renderRow)))));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = ({ owners }) => owners ? renderOwners(owners) : null;
//# sourceMappingURL=OwnersTable.js.map