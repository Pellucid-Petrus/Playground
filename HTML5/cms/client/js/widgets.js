angular.module('ng')
    //*************** WELCOME SCREEN ***************
    .directive('welcomeScreen', function () {
        return {
            restrict: 'AE',
            templateUrl: 'welcomescreen.html',
            scope: false // uses parent scope
        };
    })
    // animation for welcome screen
    .directive('swipeUp', ['$document', function($document) {
        return function(scope, element, attr) {
            var startY = 0, y = 0;

            element.on('mousedown', function(event) {
                // Prev<<ent default dragging of selected content
                event.preventDefault();
                startY = event.pageY - y;
                $document.on('mousemove', mousemove);
                $document.on('mouseup', mouseup);
            });

            function mousemove(event) {
                if (y > 0)
                    return;
                y = event.pageY - startY;
                element.css({
                    top: y + 'px'
                });
            }

            function mouseup() {
                console.log(element.height());
                TweenMax.to(element, 2, {y: -(element.height()*2) });

                $document.off('mousemove', mousemove);
                $document.off('mouseup', mouseup);
            }
        };
    }]);