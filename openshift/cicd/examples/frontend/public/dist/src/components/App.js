"use strict";
const React = require('react');
const Menu_1 = require('./Menu');
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = ({ location, children }) => (React.createElement("div", null, 
    React.createElement(Menu_1.default, {name: location.pathname}), 
    React.createElement("div", {className: 'container-fluid'}, 
        React.createElement("div", {className: 'container xd-container'}, 
            children, 
            React.createElement("div", {className: 'container'}, 
                React.createElement("div", {className: 'row'}, 
                    React.createElement("div", {className: 'col-12 text-center'}, 
                        React.createElement("img", {src: '/images/spring-pivotal-logo.png', alt: 'Sponsored by Pivotal'})
                    )
                )
            ))
    )));
//# sourceMappingURL=App.js.map