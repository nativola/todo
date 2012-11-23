class User < ActiveRecord::Base
  has_many :tasks
  attr_accessible :name
  validates_presence_of :name
end
