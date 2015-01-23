from setuptools import setup, find_packages

version = '1.0.0'
with open('version') as f:
    version = f.readline()

# WARNING: copy this to build/thrift/py before running setup.py
setup(
    name='knowsis.stanford-corenlp.thrift',
    version=version,
    # package_dir={'': 'build/thrift/py'},
    # packages=find_packages('build/thrift/py/corenlp'),
    packages=[
        'corenlp'
    ],
    include_package_data=True,
    description='Thrift client for Stanford CoreNLP service',
    author='Knowsis Ltd',
    author_email='dev@knows.is',
    url="https://github.com/knowsis/stanford-thrift"
)
