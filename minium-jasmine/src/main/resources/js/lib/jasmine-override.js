var origFormat = jasmine.PrettyPrinter.prototype.format;
jasmine.PrettyPrinter.prototype.format = function(value) {
  if (value && value instanceof Packages.java.lang.Object) value = String(value.toString());
  return origFormat.call(this, value);
};

var origExpect = expect;
expect = function(actual) {
  if (actual && actual instanceof Packages.java.lang.String) actual = String(actual);
  return origExpect.call(this, actual);
}

// matchers
beforeEach(function() {
  
  var empty = function(matcher, elems) {
    return matcher.isNot ? !checkNotEmpty(elems) : checkEmpty(elems);
  };
  
  var notEmpty = function(matcher, elems) {
    return matcher.isNot ? !checkEmpty(elems) : checkNotEmpty(elems);
  };
  
  this.addMatchers({
    toBeEmpty : function() {
      var not = this.isNot ? " not " : " ";
      this.message = function() {
        return "Expected webElements " + this.actual + not + "to be empty";
      };
      return empty(this, this.actual);
    },
    
    toHaveSize : function(expected) {
      var size = this.actual.size();
      this.message = function() {
        return [
          "Expected webElements " + this.actual + " to have size " + expected + " but it has " + size,
          "Expected webElements " + this.actual + " not to have size " + expected
        ];
      };
      return size === expected;
    },

    toHaveText : function(expected) {
      var not = this.isNot ? " not " : " ";
      this.message = function() {
          return "Expected webElements " + this.actual + not + "to have text '" + expected + "'";
      };
      var filtered = this.actual.withText(expected);
      return notEmpty(this, filtered);
    },

    toContainText : function(expected) {
      var not = this.isNot ? " not " : " ";
      this.message = function() {
        return "Expected webElements " + this.actual + not + "to contain text '" + expected + "'";
      };
      var filtered = this.actual.containingText(expected);
      return notEmpty(this, filtered);
    },

    toHaveAttr : function(key, expected) {
      var not = this.isNot ? " not " : " ";
      this.message = function() {
        return "Expected webElements " + this.actual + not + "to have attribute '" + key + "' with value '" + expected + "'";
      };
      var filtered = this.actual.withAttr(key, expected);
      return notEmpty(this, filtered);
    }
  });
})

