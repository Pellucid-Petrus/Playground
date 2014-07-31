angular.module('ng')
.directive('titleBar', function () {
    return {
        restrict: 'AE',
        templateUrl: 'titlebar.html',
        scope: false // uses parent scope
    };
});