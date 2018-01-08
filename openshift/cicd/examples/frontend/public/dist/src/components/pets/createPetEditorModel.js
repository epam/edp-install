"use strict";
const util_1 = require('../../util');
const toSelectOptions = (pettypes) => pettypes.map(pettype => ({ value: pettype.id, name: pettype.name }));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = (ownerId, petLoaderPromise) => {
    return Promise.all([fetch(util_1.url('/api/pettypes'))
            .then(response => response.json())
            .then(toSelectOptions),
        fetch(util_1.url('/api/owner/' + ownerId))
            .then(response => response.json()),
        petLoaderPromise,
    ]).then(results => ({
        pettypes: results[0],
        owner: results[1],
        pet: results[2]
    }));
};
//# sourceMappingURL=createPetEditorModel.js.map