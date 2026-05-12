$version = '4.10.14'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '72D4D669F86E88465FAB57D97DEB56C84EB6B9AAB4F38B154D7D3D265549FF0A'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
