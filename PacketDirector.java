public class PacketDirector {
    public static void constructChangeOfEnemyPositionPacket(PacketBuilder builder, Enemy enemy)
    {
        builder.setId(enemy.id).setX(enemy.getRel_x()).setY(enemy.getRel_y());
    }
    public static void constructChangeOfPlayerPositonPacket(PacketBuilder builder, Player player)
    {
        builder.setId(player.id).setX(player.getRel_x()).setY(player.getRel_y());
    }
    public static void constructDamagePlayerPacket(PacketBuilder builder, Player player)
    {
        builder.setId(player.id).setHP(player.getHP());
    }
    public static void constructPlayerAttackPacket(PacketBuilder builder, Tile tile)
    {
        builder.setX(tile.getTrel_x()).setY(tile.getTrel_y());
    }
}
