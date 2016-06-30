(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .controller('UserInfoDialogController', UserInfoDialogController);

    UserInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'UserInfo', 'User', 'Address'];

    function UserInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, UserInfo, User, Address) {
        var vm = this;

        vm.userInfo = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.addresss = Address.query({filter: 'userinfo-is-null'});
        $q.all([vm.userInfo.$promise, vm.addresss.$promise]).then(function() {
            if (!vm.userInfo.addressId) {
                return $q.reject();
            }
            return Address.get({id : vm.userInfo.addressId}).$promise;
        }).then(function(address) {
            vm.addresses.push(address);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userInfo.id !== null) {
                UserInfo.update(vm.userInfo, onSaveSuccess, onSaveError);
            } else {
                UserInfo.save(vm.userInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('computerWebstoreApp:userInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.birthDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
