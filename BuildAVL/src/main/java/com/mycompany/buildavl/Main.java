
package com.mycompany.buildavl;

import java.util.Scanner;

/* 
Jenny Ximena Ordoñez Espinosa - Realizó el procesamiento de inputs
Jhon Sebastián Moreno Triana - Juntó todas las clases existentes y orquestó el correcto funcionamiento entre ellas.
José Simón Ramos Sandoval - Modificó el heap para que cumpliera con los requerimientos del problema.
Angie Sofía Cárdenas Sánchez - Diseñó el heap para implementarlo.
Juan David Guarnizo Gutierrez - Implementó el árbol AVL.
Juan Felipe Hincapié Gómez -  Sugirió la idea para la simulación del servicio de pasaportes.
 */

public class Main {
    public static class Node<T> {
        T data;
        Node next;

        public Node(T item){
            this.data = item;
            this.next = null;
        }
    }
    
    public static class Queue<T> {
        private Node<T> front, rear;

        public Queue(){
            this.front=null;
            this.rear=null;
        }

        public void enqueue(T item){
            Node newNode = new Node(item);
            if(this.rear!=null){
                this.rear.next=newNode;
                this.rear=this.rear.next;}
            else{
                this.front = newNode;
                this.rear = this.front;
            }
        }

        public T dequeue(){
            if(!isEmpty()){
                T d = this.front.data;
                if(this.front==this.rear){
                    this.rear=null;
                }
                this.front = this.front.next;
                return d;
            }else{
                System.out.println("Empty queue.");
                return null;
            }

        }

        public boolean isEmpty(){
            return this.front == null;
        }

        public void printQueue(){
            while(!this.isEmpty()){
                System.out.println(this.dequeue());
            }
        }
    }
    
    public static class AVLNode<T> {
        public AVLNode<T> parent, left, right;
        public T data;
        public int height;

        public AVLNode(T item){
            data = item;
        }
    }
    
    public static class AVL {
        public AVLNode<Integer> root;
        public Queue<Integer> path = new Queue<Integer>();

        public AVLNode<Integer> find(Integer key, AVLNode<Integer> init){
            if(init.data == key){
                return init;
            }else if(init.data > key){
                if(init.left!=null){
                    return find(key, init.left);
                }else{
                    return init;
                }
            }else if(init.data < key){
                if(init.right!=null){
                    return find(key, init.right);
                }else{
                    return init;
                }
            }
            return null;
        }

        private void insert(Integer key){     
            if(root == null){
                root = new AVLNode<Integer>(key);
                root.height = 1;
            }else{
                AVLNode<Integer> p = find(key,root);
                if(p.data < key){
                    p.right  = new AVLNode<Integer>(key);
                    p.right.parent = p;
                    p.right.height = 1;
                }else if (p.data>key){
                    p.left  = new AVLNode<Integer>(key);
                    p.left.parent = p;
                    p.left.height = 1;
                }
            }
        }

        public void AVLInsert(int num){
            insert(num);
            AVLNode<Integer> node = find(num,root);
            balance(node);
            while(root.parent != null){
                root = root.parent;
            }
        }

        private int[] getChildrenHeights(AVLNode<Integer> node){
            int[] heights = new int[2];
            if(node.right!=null){
                heights[1] = node.right.height;
            }else{heights[1]=0;}
            if(node.left!=null){
                heights[0] = node.left.height;
            }else{heights[0]=0;}
            return heights;
        }

        private void adjustHeight(AVLNode<Integer> node){
            if(node == null){return;}
            int[] heights = getChildrenHeights(node);

            if(heights[1]>heights[0]){
                node.height = heights[1]+1;
            }else{
                node.height = heights[0]+1;
            } 
        }

        private void balance(AVLNode<Integer> node){
            int[] heights = getChildrenHeights(node);
            adjustHeight(node.parent);

            if(heights[0]-heights[1]>1){
                balanceLeft(node);
            }
            if(heights[1]-heights[0]>1){
                balanceRight(node);
            }

            if(node.parent!=null){
                balance(node.parent);
            }
        }

        private void balanceLeft(AVLNode<Integer> node){
            int[] heights = getChildrenHeights(node.left);

            if(heights[1]>heights[0]){
                rotateLeft(node.left);
            }

            rotateRight(node);
        }

        private void balanceRight(AVLNode<Integer> node){
            int[] heights = getChildrenHeights(node.right);

            if(heights[0]>heights[1]){
                rotateRight(node.right);
            }
            rotateLeft(node);
        }

        private void rotateRight(AVLNode<Integer> node){          
            node.left.parent = node.parent;

            if(node.parent!=null){
                if(node.parent.left == node){
                    node.parent.left = node.left;
                }else{
                    node.parent.right = node.left;
                }
            }

            node.parent = node.left;

            if(node.left.right!=null){
                node.parent.right.parent = node;
                node.left = node.left.right;
            }else{
                node.left = null;
            }

            node.parent.right = node;

            adjustHeight(node);
            adjustHeight(node.parent);
            adjustHeight(node.parent.parent);
        }

        private void rotateLeft(AVLNode<Integer> node){
            node.right.parent = node.parent;

            if(node.parent!=null){
                if(node.parent.left == node){
                    node.parent.left = node.right;
                }else{
                    node.parent.right = node.right;
                }
            }

            node.parent = node.right;


            if(node.right.left!=null){
                node.right.left.parent = node;
                node.right = node.right.left;
            }else{
                node.right = null;
            }

            node.parent.left = node;

            adjustHeight(node);
            adjustHeight(node.parent);
            adjustHeight(node.parent.parent);
        }

        public void preOrder(AVLNode<Integer> node){
            path.enqueue(node.data);
            if(node.left != null){
                preOrder(node.left);
            }
            if(node.right!=null){
                preOrder(node.right);
            }
        }

        public void preOrder(){
            preOrder(root);
        }

        public void printPreOrder(){
            preOrder();
            while(!path.isEmpty()){
                System.out.println(path.dequeue());
            }
        }
    }
    
    public static class Gestor{
        private Scanner sc = new Scanner(System.in);
        private AVL tree = new AVL();
        
        public void run(){
            read();
            write();
        }
        
        public void read(){
            sc.nextLine();
            String[] linkedList = sc.nextLine().replace("[","").replace("]","").split(",");
            
            for(String s: linkedList){
                tree.AVLInsert(Integer.valueOf(s));
            }
        }
        
        public void  write(){
            String treeString = "[";
            tree.preOrder();
            while(!tree.path.isEmpty()){
                treeString += String.valueOf(tree.path.dequeue())+",";
            }
            treeString = treeString.substring(0, treeString.length()-1)+"]";
            System.out.println(treeString);
        }
    }

    public static void main(String[] args) {
        Gestor gestor =  new Gestor();
        gestor.run();
    }
}