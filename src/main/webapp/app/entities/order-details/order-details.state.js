(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('order-details', {
            parent: 'entity',
            url: '/order-details?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OrderDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order-details/order-details.html',
                    controller: 'OrderDetailsController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('order-details-detail', {
            parent: 'entity',
            url: '/order-details/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OrderDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order-details/order-details-detail.html',
                    controller: 'OrderDetailsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'OrderDetails', function($stateParams, OrderDetails) {
                    return OrderDetails.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('order-details.new', {
            parent: 'order-details',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-details/order-details-dialog.html',
                    controller: 'OrderDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                sum: null,
                                quantity: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('order-details', null, { reload: true });
                }, function() {
                    $state.go('order-details');
                });
            }]
        })
        .state('order-details.edit', {
            parent: 'order-details',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-details/order-details-dialog.html',
                    controller: 'OrderDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrderDetails', function(OrderDetails) {
                            return OrderDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-details', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order-details.delete', {
            parent: 'order-details',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-details/order-details-delete-dialog.html',
                    controller: 'OrderDetailsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OrderDetails', function(OrderDetails) {
                            return OrderDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-details', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
