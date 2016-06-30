(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .controller('UserInfoDetailController', UserInfoDetailController);

    UserInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'UserInfo', 'User', 'Address'];

    function UserInfoDetailController($scope, $rootScope, $stateParams, entity, UserInfo, User, Address) {
        var vm = this;

        vm.userInfo = entity;

        var unsubscribe = $rootScope.$on('computerWebstoreApp:userInfoUpdate', function(event, result) {
            vm.userInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
