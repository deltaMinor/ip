package her.m35.command;

import java.util.Random;

import her.m35.Storage;
import her.m35.TaskList;
import her.m35.Ui;

/**
 * Command that displays a random quote or fact about Hermes.
 */
public class QuoteCommand extends Command {

    private static final String[] quoteOpenings = {
        "You wanna hear a quote about the great god Hermes? Here you go:\n",
        "It's always a good day to learn something new, so here's a quote for ya:\n",
        "I've chosen this line just for you:\n",
        "I was just reading, and I think you might like this quote I found:\n",
        "Oh, I know! You'd love this one:\n",
        "Now this is a fire line:\n",
        "I travelled far and wide to find a good quote, and I think this will do nicely:\n",
        "If you're an aspiring writer, this line might be worth looking into:\n",
        "Oh this one is good, I hope it inspires you:\n",
        "Looking for a quote about Hermes? I hope this will fit you:\n"
    };

    private record Quote(String quote, String source) {
    }

    private final Quote[] quotes = {
        new Quote(
                "You know the saying, don’t kill the messenger? Hold that thought really, really close to your hearts.",
                "Hermes, in Dark-Hunter by Sherrilyn Kenyon"),
        new Quote(
                "If there were a god of New York, it would be the Greek's Hermes, the Roman's Mercury. He embodies"
                       + "New York qualities: the quick exchange, the fastness of language and style, craftiness, the"
                       + "mixing of people and crossing of borders, imagination.",
                "James Hillman"),
        new Quote(
                "The Bird of Hermes is my name, Eating my Wings to make me tame.",
                "Alucard, in Hellsing by Kohta Hirano"),
        new Quote(
                "Snakes are sometimes perceived as evil, but they are also perceived as medicine. If you look at an"
                + "ambulance, there's the two snakes on the side of the ambulance. The caduceus, or the staff of Hermes"
                + ", there's the two snakes going up it, which means that the venom can also be healing.",
                "Nicolas Cage"),
        new Quote(
                "I see Hermes, unsuspected, dying, well-beloved, saying to the people, \"Do not weep for me, This is"
                + "not my true country, I have lived banished from my true country - I now go back there, I return to "
                + "the celestial sphere where every one goes in his turn.\"",
                "Walt Whitman"),
        new Quote(
                "The severe schools shall never laugh me out of the philosophy of Hermes, that this visible world is"
                + "but a picture of the invisible, wherein as in a portrait, things are not truly, but in equivocal "
                + "shapes, and as they counterfeit some real substance in that invisible fabric.",
                "Thomas Browne")
    };

    private final String[] facts = {
        "You probably already know this, but Hermes primarily served as the herald or messenger of the gods.",
        "Did you know? Hermes was also the god of shepherds, land travel, and literature. "
            + "He once also had the role of conducting souls to Hades.",
        "Hermes is most often presented as a graceful youth, wearing a winged hat and winged sandals.",
        "Hermes was often considered a trickster due to his cunning and clever personality. How fun!",
        "Fun fact: Hermes was born in a cave on a mountain in Arcadia; "
            + "he was conceived and born within the course of one day.",
        "Here's a story for ya: Hermes was also patron of thieves. On the day of his birth, Hermes stole cattle from "
            + "Apollo. After he was caught, Hermes still managed to win Apollo over by playing a few songs on his lyre "
            + "and gifting it to him. This was how Hermes became one of the twelve Olympian gods!"
    };

    /**
     * Constructs a QuoteCommand.
     */
    public QuoteCommand() {
    }

    /**
     * {@inheritDoc}
     *
     * Chooses a random quote or fact and displays it to the user.
     */
    @Override
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        Random random = new Random();
        if (random.nextInt(2) == 0) {
            int quoteIndex = random.nextInt(quotes.length);
            ui.printMessage(
                    quoteOpenings[random.nextInt(quoteOpenings.length)],
                    "$\"" + quotes[quoteIndex].quote() + "\"\n",
                    " -" + quotes[quoteIndex].source());
        } else {
            ui.printMessage(facts[random.nextInt(facts.length)]);
        }
    }
}
