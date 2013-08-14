function ListSettingController($scope, $routeParams, $location, $dialog, Settings, SettingUpdator, messageService) {

       Settings.get(function (data){
           $scope.settings = data.settings;
       });

    $scope.saveSettings = function(){
        SettingUpdator.post({}, $scope.settings, function (data){
            var dialogOpts = {
                id: "settingsSavedConfirmation",
                header: messageService.get('confirm.settings.saved'),
                body: messageService.get('confirm.settings.saved.detail')
            };
            OpenLmisDialog.newDialog(dialogOpts, $scope.clearSelectedFacility, $dialog, messageService);
        });
    }
}