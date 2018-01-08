"use strict";
var __assign = (this && this.__assign) || Object.assign || function(t) {
    for (var s, i = 1, n = arguments.length; i < n; i++) {
        s = arguments[i];
        for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
            t[p] = s[p];
    }
    return t;
};
const React = require('react');
const util_1 = require('../../util');
const LoadingPanel_1 = require('./LoadingPanel');
const PetEditor_1 = require('./PetEditor');
const createPetEditorModel_1 = require('./createPetEditorModel');
;
class EditPetPage extends React.Component {
    componentDidMount() {
        const { params } = this.props;
        const fetchUrl = util_1.url(`/api/owners/${params.ownerId}/pets/${params.petId}`);
        const loadPetPromise = fetch(fetchUrl).then(response => response.json());
        createPetEditorModel_1.default(this.props.params.ownerId, loadPetPromise)
            .then(model => this.setState(model));
    }
    render() {
        if (!this.state) {
            return React.createElement(LoadingPanel_1.default, null);
        }
        return React.createElement(PetEditor_1.default, __assign({}, this.state));
    }
}
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = EditPetPage;
//# sourceMappingURL=EditPetPage.js.map