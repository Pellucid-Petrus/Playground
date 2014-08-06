// http is a service. We add it to our array of dependencies
angular.module('cms_app', ['ngAnimate', 'ngTouch'])
    // when we use $http as argument, this is called dependency injection
    // when angularJS loads, it creates a "injector".
    // when the built-in services load they register them to the injector as available libraries
    // then here the controller gets executed, the injector pass as arguments the services
    .controller('MainCtrl', function ($scope, $http) {
        var user = "fabio";
        var appid = "1234";
        // fetches data from the internet
        //$scope.data_url = "https://raw.githubusercontent.com/gnuton/Playground/master/HTML5/cms/client/data/test/app_data.json";
        $scope.data_url ="http://localhost:5000/get/" + user + "/" + appid + "/?callback=JSON_CALLBACK";
        $http.jsonp($scope.data_url)
            .success(function (data) {
                $scope.appTitle = data["title"];
                $scope.sidemenuitems = data["sidemenu"];
                $scope.pages = data["pages"];
            }).error(function (data, status, headers, config) {
                //this always gets called
                console.log(status);
            });

        // Initialize vars
        $scope.appTitle = "Mobile CMS is loading...";
        $scope.sidemenuitems = [
            {name: "loading...", pageID: -1 },
            {name: "About Bonsai", pageID: 0}
        ];
        $scope.pages = [{"title" : "About", "content":"some content<p>Error: <code>.error</code></p>"}];
        $scope.slides = [
            {image: 'data/test/images/img00.jpg', description: 'Image 00'},
            {image: 'data/test/images/img01.jpg', description: 'Image 01'}
        ];

        //*** GALLERY WIDGET **//
        $scope.direction = 'left';
        $scope.currentIndex = 0;

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

        //*** TITLEBAR/SIDEBAR ***//
        $scope.toggleMenu = function() {
            var element = document.getElementById("mainlayout");
            var isMenuOpen = (element.style.left === "500px");
            console.log(isMenuOpen);
            if (isMenuOpen)
                TweenMax.to(element, 0.5, {left: 0 });
            else
                TweenMax.to(element, 0.5, {left: 500 });
        }

        $scope.showPage = function(idx) {
            var element = document.getElementById("mainlayout");
            console.log(idx);
            $scope.toggleMenu();
            $scope.pageTitle = $scope.pages[idx].title;
            $scope.pageContent = $scope.pages[idx].content;
        }

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
