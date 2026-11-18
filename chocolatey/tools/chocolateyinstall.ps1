$version = '5.2.7'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'D3C8C294946107DBAB5821C67C9281C1A8F219EECB7AA9D847768B42B9609001'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
