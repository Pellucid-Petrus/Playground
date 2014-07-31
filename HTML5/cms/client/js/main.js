// http is a service. We add it to our array of dependencies
angular.module('cms_app', ['ngAnimate', 'ngTouch'])
    // when we use $http as argument, this is called dependency injection
    // when angularJS loads, it creates a "injector".
    // when the built-in services load they register them to the injector as available libraries
    // then here the controller gets executed, the injector pass as arguments the services
    .controller('MainCtrl', function ($scope, $http) {
        $scope.data_url = "https://raw.githubusercontent.com/gnuton/Playground/master/HTML5/cms/client/data/test/app_data.json";
        $http.get($scope.data_url).success(function (data) {
            $scope.slides = data["pages"];
        });

        $scope.slides = [
            {image: 'data/test/images/img00.jpg', description: 'Image 00'}
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
