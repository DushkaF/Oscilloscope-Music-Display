# Oscilloscope-Music-Display
In this repo students from Department of Physics makes vectorise oscilloscope  display for playing in the game

# Архитектура:

1. **Engine** осуществляет управление над всеми процессами (отсчитывает время, отдает команды итд)

2. В **input** хранятся все классы отвечающие за ввод информации для обработки. *Input* - класс интерфейс, через который должно происходить все взаимодействие Engine с вводом (и только через него!) . *Picture* -объект для хранения информации об изображении для отрисовки и самого иозбражения. *InputArgs* - вспомогательный класс для хранения параметров чтения (путь, режим итд). 

3. В **edges** хранятся все классы отвечающие за обработку изображения в растр с краями. *EdgMain* - класс интерфейс, через который должно происходить все взаимодействие Engine с обработкой изображения в края (и только через него!). *Edges* - класс оболочка для хранения растра с краями (матрицы из 0 и 1).

4. В **vectors** хранятся все классы отвечающие за преобразование растра в список векторов. *VecMain* - класс интерфейс, через который должно происходить все взаимодействие Engine с обработкой растра в вектор (и только через него!). *Figures* - класс оболочка для хранения списка фигур. *Figure* - класс фигур - списка векторов, образующих одну фигуру (т.е. связный граф). *Vector* - класс вектора (начало, конец, длина итд).
 
5. В **map** хранятся все классы отвечающие за преобразование фигур в карту порядка рисования на осциллографе. *MapMain* - класс интерфейс, через который должно происходить все взаимодействие Engine с созданием карты рисования (и только через него!). *Map* - класс оболочка для хранения карты порядка рисования.

6. В **output** хранятся все классы отвечающие за вывод информации на осциллограф. *Output* - класс интерфейс, через который должно происходить все взаимодействие Engine с выводом (и только через него!). *OutputArgs* - вспомогательный класс для хранения параметров вывода (формат вывода, путь, канал итд)... 

