"use strict";
const React = require('react');
const react_router_1 = require('react-router');
const VisitsTable = ({ ownerId, pet }) => (React.createElement("table", {className: 'table-condensed'}, 
    React.createElement("thead", null, 
        React.createElement("tr", null, 
            React.createElement("th", null, "Visit Date"), 
            React.createElement("th", null, "Description"))
    ), 
    React.createElement("tbody", null, 
        pet.visits.map(visit => (React.createElement("tr", {key: visit.id}, 
            React.createElement("td", null, visit.date), 
            React.createElement("td", null, visit.description)))), 
        React.createElement("tr", null, 
            React.createElement("td", null, 
                React.createElement(react_router_1.Link, {to: `/owners/${ownerId}/pets/${pet.id}/edit`}, "Edit Pet")
            ), 
            React.createElement("td", null, 
                React.createElement(react_router_1.Link, {to: `/owners/${ownerId}/pets/${pet.id}/visits/new`}, "Add Visit")
            )))));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = ({ owner }) => (React.createElement("section", null, 
    React.createElement("h2", null, "Pets and Visits"), 
    React.createElement("table", {className: 'table table-striped'}, 
        React.createElement("tbody", null, owner.pets.map(pet => (React.createElement("tr", {key: pet.id}, 
            React.createElement("td", {style: { 'verticalAlign': 'top' }}, 
                React.createElement("dl", {className: 'dl-horizontal'}, 
                    React.createElement("dt", null, "Name"), 
                    React.createElement("dd", null, pet.name), 
                    React.createElement("dt", null, "Birth Date"), 
                    React.createElement("dd", null, pet.birthDate), 
                    React.createElement("dt", null, "Type"), 
                    React.createElement("dd", null, pet.type.name))
            ), 
            React.createElement("td", {style: { 'verticalAlign': 'top' }}, 
                React.createElement(VisitsTable, {ownerId: owner.id, pet: pet})
            )))))
    )));
//# sourceMappingURL=PetsTable.js.map