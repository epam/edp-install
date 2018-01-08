"use strict";
const React = require('react');
const OwnerEditor_1 = require('./OwnerEditor');
const util_1 = require('../../util');
class EditOwnerPage extends React.Component {
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
        const owner = this.state && this.state.owner;
        if (owner) {
            return React.createElement(OwnerEditor_1.default, {initialOwner: owner});
        }
        return null;
    }
}
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = EditOwnerPage;
//# sourceMappingURL=EditOwnerPage.js.map