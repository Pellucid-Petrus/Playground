

var menuState = {

    create: function() {
        var logo = game.add.sprite(w/2, -150, 'logo');
        logo.inputEnabled = true;
        logo.events.onInputDown.add(this.start, this);
        logo.anchor.setTo(0.5, 0.5);
        game.add.tween(logo).to({ y: h/2-20 }, 1000, Phaser.Easing.Bounce.Out).start();

        var label = game.add.text(w/2, h-60, 'tap/click the logo to start!', { font: '20px Arial', fill: '#fff' });
        label.anchor.setTo(0.5, 0.5);
        label.alpha = 0;
        game.add.tween(label).delay(500).to({ alpha: 1}, 500).start();
        game.add.tween(label).to({y: h-70}, 500).to({y: h-50}, 500).loop().start();
    },

    start: function () {
        this.game.state.start('play');
    }

};