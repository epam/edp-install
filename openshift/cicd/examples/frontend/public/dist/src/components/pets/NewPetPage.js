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
const LoadingPanel_1 = require('./LoadingPanel');
const PetEditor_1 = require('./PetEditor');
const createPetEditorModel_1 = require('./createPetEditorModel');
;
const NEW_PET = {
    id: null,
    isNew: true,
    name: '',
    birthDate: null,
    typeId: null
};
class NewPetPage extends React.Component {
    componentDidMount() {
        createPetEditorModel_1.default(this.props.params.ownerId, Promise.resolve(NEW_PET))
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
exports.default = NewPetPage;
//# sourceMappingURL=NewPetPage.js.map