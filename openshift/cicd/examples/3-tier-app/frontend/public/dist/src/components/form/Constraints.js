"use strict";
exports.NotEmpty = {
    message: 'Enter at least one character',
    validate: (value) => {
        return !!value && value.length > 0;
    }
};
exports.Digits = (digits) => {
    const reg = new RegExp('^\\d{1,' + digits + '}$');
    return {
        message: 'Must be a number with at most ' + digits + ' digits',
        validate: (value) => {
            return !!value && value.match(reg) !== null;
        }
    };
};
//# sourceMappingURL=Constraints.js.map