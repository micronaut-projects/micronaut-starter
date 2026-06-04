$version = '4.10.16'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '1AF3E7F81C6DC074D88D1984F72D3A1DEA3CEC27DB31F01275FC2D6D3A191A13'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
