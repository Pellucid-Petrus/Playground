var playState = {
    create: function () {
        // sky
        var sky = game.add.image(0, 0, 'sky');
        sky.fixedToCamera = true;

        //  Activate the Ninja physics system
        game.physics.startSystem(Phaser.Physics.NINJA);

        // Map tiles
        var map = game.add.tilemap('map');
        map.addTilesetImage('kenney');
        var layer = map.createLayer('Tile Layer 1');
        layer.resizeWorld();
        
        // In Ninja the Tiles have an ID from 0 to 33, where 0 is 'empty', 1 is a full tile, 2 is a 45-degree slope, etc.
        var slopeMap = { '32': 1, '77': 1, '95': 2, '36': 3, '137': 3, '140': 2 };
        this.tiles = game.physics.ninja.convertTilemap(map, layer, slopeMap);

        // add player
        sprite1 = game.add.sprite(50, 50, 'ball');
        game.physics.ninja.enableCircle(sprite1, sprite1.width / 2);

        // Camera
        game.camera.follow(sprite1);

        // inputs
        this.cursors = game.input.keyboard.createCursorKeys();
    },

    update: function() {
        // collision detection
        for (var i = 0; i < this.tiles.length; i++)
        {
            sprite1.body.circle.collideCircleVsTile(this.tiles[i].tile);
        }

        // input movements
        if (this.cursors.left.isDown)
        {
            sprite1.body.moveLeft(20);
        }
        else if (this.cursors.right.isDown)
        {
            sprite1.body.moveRight(20);
        }

        if (this.cursors.up.isDown)
        {
            sprite1.body.moveUp(20);
        }
        else if (this.cursors.down.isDown)
        {
            sprite1.body.moveUp(20);
        }

    },

    render: function() {}
}