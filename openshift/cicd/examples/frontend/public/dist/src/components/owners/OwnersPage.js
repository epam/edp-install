"use strict";
const React = require('react');
const util_1 = require('../../util');
const OwnerInformation_1 = require('./OwnerInformation');
const PetsTable_1 = require('./PetsTable');
class OwnersPage extends React.Component {
    constructor() {
        super();
        this.state = {};
    }
    componentDidMount() {
        const { params } = this.props;
        if (params && params.ownerId) {
            const fetchUrl = util_1.url(`/api/owner/${params.ownerId}`);
            fetch(fetchUrl)
                .then(response => response.json())
                .then(owner => this.setState({ owner }));
        }
    }
    render() {
        const { owner } = this.state;
        if (!owner) {
            return React.createElement("h2", null, "No Owner loaded");
        }
        return (React.createElement("span", null, 
            React.createElement(OwnerInformation_1.default, {owner: owner}), 
            React.createElement(PetsTable_1.default, {owner: owner})));
    }
}
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = OwnersPage;
//# sourceMappingURL=OwnersPage.js.map