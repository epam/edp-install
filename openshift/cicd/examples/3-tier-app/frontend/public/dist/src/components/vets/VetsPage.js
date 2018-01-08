"use strict";
const React = require('react');
const util_1 = require('../../util');
class VetsPage extends React.Component {
    constructor() {
        super();
        this.state = { vets: [] };
    }
    componentDidMount() {
        const requestUrl = util_1.url('api/vets');
        fetch(requestUrl)
            .then(response => response.json())
            .then(vets => { console.log('vets', vets); this.setState({ vets }); });
    }
    render() {
        const { vets } = this.state;
        if (!vets) {
            return React.createElement("h2", null, "Veterinarians");
        }
        return (React.createElement("span", null, 
            React.createElement("h2", null, "Veterinarians"), 
            React.createElement("table", {className: 'table table-striped'}, 
                React.createElement("thead", null, 
                    React.createElement("tr", null, 
                        React.createElement("th", null, "Name"), 
                        React.createElement("th", null, "Specialties"))
                ), 
                React.createElement("tbody", null, vets.map(vet => (React.createElement("tr", {key: vet.id}, 
                    React.createElement("td", null, 
                        vet.firstName, 
                        " ", 
                        vet.lastName), 
                    React.createElement("td", null, vet.specialties.length > 0 ? vet.specialties.map(specialty => specialty.name).join(', ') : 'none'))))))));
    }
}
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = VetsPage;
//# sourceMappingURL=VetsPage.js.map