angular.module('cms_app', ['ngAnimate', 'ngTouch'])
    .controller('MainCtrl', function ($scope) {
        $scope.slides = [
            {image: 'data/test/images/img00.jpg', description: 'Image 00'},
            {image: 'data/test/images/img01.jpg', description: 'Image 01'},
            {image: 'data/test/images/img02.jpg', description: 'Image 02'},
            {image: 'data/test/images/img03.jpg', description: 'Image 03'},
            {image: 'data/test/images/img04.jpg', description: 'Image 04'}
        ];

        $scope.direction = 'left';
        $scope.currentIndex = 0;

        $scope.fetchData = function ()
        {
            $http({method: 'POST', url: 'js/posts.json'}).success(function(data)
            {
                $scope.data = data; // response data
            });
        }

        $scope.setCurrentSlideIndex = function (index) {
            $scope.direction = (index > $scope.currentIndex) ? 'left' : 'right';
            $scope.currentIndex = index;
        };

        $scope.isCurrentSlideIndex = function (index) {
            return $scope.currentIndex === index;
        };

        $scope.prevSlide = function () {
            $scope.direction = 'left';
            $scope.currentIndex = ($scope.currentIndex < $scope.slides.length - 1) ? ++$scope.currentIndex : 0;
        };

        $scope.nextSlide = function () {
            $scope.direction = 'right';
            $scope.currentIndex = ($scope.currentIndex > 0) ? --$scope.currentIndex : $scope.slides.length - 1;
        };
    })
    .animation('.slide-animation', function () {
        return {
            beforeAddClass: function (element, className, done) {
                var scope = element.scope();

                if (className == 'ng-hide') {
                    var finishPoint = element.parent().width();
                    if(scope.direction !== 'right') {
                        finishPoint = -finishPoint;
                    }
                    TweenMax.to(element, 0.5, {left: finishPoint, onComplete: done });
                }
                else {
                    done();
                }
            },
            removeClass: function (element, className, done) {
                var scope = element.scope();

                if (className == 'ng-hide') {
                    element.removeClass('ng-hide');

                    var startPoint = element.parent().width();
                    if(scope.direction === 'right') {
                        startPoint = -startPoint;
                    }

                    TweenMax.fromTo(element, 0.5, { left: startPoint }, {left: 0, onComplete: done });
                }
                else {
                    done();
                }
            }
        };
    });
