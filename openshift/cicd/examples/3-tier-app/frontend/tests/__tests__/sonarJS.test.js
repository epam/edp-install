const sonar = require('../../src/sonar/sonarJS');

describe('Sonar test', () => {
    it('should return false', () => {
        expect(sonar('')).toBe(false);
    });

    it('should return false', () => {
        expect(sonar(2)).toBe(false);
    });
});
