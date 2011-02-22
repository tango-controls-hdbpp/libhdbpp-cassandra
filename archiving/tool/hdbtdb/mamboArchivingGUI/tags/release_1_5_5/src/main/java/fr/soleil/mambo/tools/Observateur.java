package fr.soleil.mambo.tools;

// Interface implémentée par tous les observateurs.
public interface Observateur
{
        // Méthode appelée automatiquement lorsque l'état (position ou précision) du GPS change.
        public void update(Observable o);
}