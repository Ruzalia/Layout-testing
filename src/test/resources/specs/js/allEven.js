this.allEven = function (pattern) {
    var size = count(pattern);
    var parameters = "";
    if (size > 1) {
       for (var i = 2; i <= size; i+=2) {
           parameters = parameters + i + "," ;
       }
    }

    return parameters;
};